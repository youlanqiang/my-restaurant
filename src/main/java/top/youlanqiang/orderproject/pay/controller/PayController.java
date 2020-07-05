package top.youlanqiang.orderproject.pay.controller;



import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeCancelModel;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import top.youlanqiang.orderproject.backmanager.service.OrderService;
import top.youlanqiang.orderproject.backmanager.service.TableService;
import top.youlanqiang.orderproject.core.entity.Order;
import top.youlanqiang.orderproject.core.entity.Result;
import top.youlanqiang.orderproject.core.entity.enums.PaymentMethod;
import top.youlanqiang.orderproject.core.exception.UnmessageException;
import top.youlanqiang.orderproject.core.mapper.OrderMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@RestController
@Api(tags = "支付接口")
@RequestMapping("/pay")
@Slf4j
public class PayController {

    @Resource
    AlipayClient alipayClient;

    @Resource
    OrderService orderService;

    @Resource
    TableService tableService;

    @Resource
    OrderMapper orderMapper;


    @PostMapping("/test/f2fpay/barCodePay")
    @ApiOperation("支付宝当面付,扫描用户支付宝付款码")
    public Result testAlipayBarCodePay(@ApiParam("用户认证码") @RequestParam("authCode") String authCode){

        //商户唯一订单号
        String outTradeNo = UUID.randomUUID().toString();
        String storeId = "mzmw";
        String subject = storeId + "餐厅订单";
        String totalAmount = "100.2";
        String timeoutExpress = "1m";

        AlipayTradePayRequest request = new AlipayTradePayRequest(); //创建API对应的request类
        AlipayTradePayModel model = new AlipayTradePayModel();
        model.setOutTradeNo(outTradeNo);
        model.setScene("bar_code");
        model.setAuthCode(authCode);
        model.setSubject(subject);
        model.setStoreId(storeId);
        model.setTimeoutExpress(timeoutExpress);
        model.setTotalAmount(totalAmount);
        request.setBizModel(model);

        try {
            AlipayTradePayResponse response = alipayClient.execute(request);
            //支付时传入的商店订单号
            String orderTrade =  response.getOutTradeNo();
            //支付宝28位交易号
            String alipayTradeNo =  response.getTradeNo();
            log.info("商店订单号:{}, 支付宝交易号:{}", orderTrade, alipayTradeNo);

            int count = 0;

            while(count < 10){
                AlipayTradeQueryResponse response1 = queryAlipayOrder(orderTrade, alipayTradeNo);
                String status = response1.getTradeStatus();
                if(status.equals("WAIT_BUYER_PAY")){
                    //等待付款
                    count++;
                }else if(status.equals("TRADE_FINISHED") || status.equals("TRADE_SUCCESS")){
                    //支付成功
                    return Result.success("交易成功。");
                }else if(status.equals("TRADE_CLOSED")){
                    //支付失败交易关闭
                    if(cancelAlipayOrder(orderTrade, alipayTradeNo)){
                        return Result.error("订单支付失败，交易关闭。");
                    }
                    return Result.error("支付支付失败，交易未关闭。");
                }

                try {
                    TimeUnit.SECONDS.sleep(5);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }


        }catch(AlipayApiException e){
            e.printStackTrace();
            return Result.error("交易失败！");
        }

        return Result.success("支付成功。");
    }

    private AlipayTradeQueryResponse queryAlipayOrder(String outTradeNo, String tradeNo)throws AlipayApiException{
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();//创建API对应的request类
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setOutTradeNo(outTradeNo);
        model.setTradeNo(tradeNo);
        request.setBizModel(model);
        AlipayTradeQueryResponse response = alipayClient.execute(request);//通过alipayClient调用API，获得对应的response类
        return response;
    }

    private boolean cancelAlipayOrder(String outTradeNo, String tradeNo) throws  AlipayApiException{
        AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();//创建API对应的request类
        AlipayTradeCancelModel model = new AlipayTradeCancelModel();
        model.setOutTradeNo(outTradeNo);
        model.setTradeNo(tradeNo);
        request.setBizModel(model);
        AlipayTradeCancelResponse response = alipayClient.execute(request);//通过alipayClient调用API，获得对应的response类
        String action = response.getAction();
        if(action.equals("close") || action.equals("refund")){
            return true;
        }
        return false;
    }


    @PostMapping("/f2fpay/barCodePay")
    @ApiOperation("支付宝当面付,扫描用户支付宝付款码")
    public Result alipayBarCodePay(@ApiParam("订单ID") @RequestParam("orderId") Integer orderId,
                                   @ApiParam("authCode") @RequestParam("onlineCode") String authCode){

        Order order = orderService.getOrderById(orderId);

        if (!order.getStatus()) {
            throw new UnmessageException("订单已取消!");
        }

        if (order.getPaymentStatus()) {
            throw new UnmessageException("订单已付款!");
        }

        //商户唯一订单号
        String outTradeNo = UUID.randomUUID().toString();
        String storeId = order.getShopName();
        String subject = storeId + "餐厅订单";
        String totalAmount = order.getActualAmount().toString();
        String timeoutExpress = "1m";

        AlipayTradePayRequest request = new AlipayTradePayRequest(); //创建API对应的request类
        AlipayTradePayModel model = new AlipayTradePayModel();
        model.setOutTradeNo(outTradeNo);
        model.setScene("bar_code");
        model.setAuthCode(authCode);
        model.setSubject(subject);
        model.setStoreId(storeId);
        model.setTimeoutExpress(timeoutExpress);
        model.setTotalAmount(totalAmount);

        request.setBizModel(model);

        try {
            AlipayTradePayResponse response = alipayClient.execute(request);
            //支付时传入的商店订单号


            String orderTrade =  response.getOutTradeNo();
            //支付宝28位交易号
            String alipayTradeNo =  response.getTradeNo();
            log.info("商店订单号:{}, 支付宝交易号:{}", orderTrade, alipayTradeNo);

            int count = 0;

            while(count < 10){
                AlipayTradeQueryResponse response1 = queryAlipayOrder(orderTrade, alipayTradeNo);
                String status = response1.getTradeStatus();
                if(status == null){
                    return Result.error("支付宝条码不对。");
                }
                if(status.equals("WAIT_BUYER_PAY")){
                    //等待付款
                    count++;
                }else if(status.equals("TRADE_FINISHED") || status.equals("TRADE_SUCCESS")){
                    //支付成功
                    order.setPaymentPrice(order.getActualAmount());
                    order.setPaymentMethod(PaymentMethod.ALIPAY);
                    order.setPaymentStatus(true);
                    order.setOnlineCode(alipayTradeNo);
                    order.setEndDate(LocalDateTime.now());

//                    if (!tableService.tableUnused(order.getTableId(), orderId)) {
//                        return Result.error("支付宝支付成功，状态管理失败，请联系管理员。");
//                    }

                    if(orderMapper.updateById(order) == 1){
                        return Result.success("支付宝支付成功。");
                    }

                    return Result.success("支付宝支付成功，状态管理失败，请联系管理员。");
                }else if(status.equals("TRADE_CLOSED")){
                    //支付失败交易关闭
                    if(cancelAlipayOrder(orderTrade, alipayTradeNo)){
                        return Result.error("订单支付失败，交易关闭。");
                    }
                    return Result.error("支付支付失败，交易未关闭。");
                }

                try {
                    TimeUnit.SECONDS.sleep(5);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        }catch(AlipayApiException e){
            e.printStackTrace();
            return Result.error("交易失败！");
        }

        return Result.success("支付成功。");
    }



    @PostMapping("/weixin/pay")
    @ApiOperation("微信扫码支付")
    public Result weixinf2f(){


        return Result.success("");
    }



}
