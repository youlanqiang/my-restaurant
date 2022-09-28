package top.youlanqiang.orderproject.backmanager.backkitchen;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import top.youlanqiang.orderproject.core.entity.Result;
import top.youlanqiang.orderproject.core.exception.UnmessageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint("/kitchen/{shopName}")
@Component
@Slf4j
/**
 * 后厨websocket
 */
public class KitchenWebsocket {


    private static ConcurrentHashMap<String, CopyOnWriteArrayList<Session>> map
            = new ConcurrentHashMap<>();

    private static final AtomicInteger count = new AtomicInteger(0);

    private Session session;

    private String shopName;

    @OnOpen
    public void onOpen(@PathParam("shopName") String shopName, Session session) {
        this.session = session;
        this.shopName = shopName;
        try {
            putSession(shopName, session);
            sendMessage(Result.success("连接成功!"));
            count.addAndGet(1);
            log.info("{}:{} 登陆, 当前人数:{}", shopName, this, count.get());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @OnMessage
    public void onMessage(String message, Session session) {
        JSONObject result = JSON.parseObject(message);
        log.info("shopName:{} 接受消息:{}",shopName, result.toJSONString());
        String shopName = result.getString("shopName");
        Integer code = result.getInteger("code");
        if(code == 301){
            WaiterWebsocket.sendMessage(shopName, Result.wsMsg("后厨通知服务员上菜"));
            return;
        }
        if(code == 302){
            WaiterWebsocket.sendMessage(shopName, Result.wsMsg("后厨呼叫服务员"));
            return;
        }

    }

    @OnClose
    public void onClose(@PathParam("shopName") String shopName) {
        removeSession(shopName, this.session);
        count.addAndGet(-1);
        log.info("{}:{} 退出, 当前人数:{}", shopName, this, count.get());
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    public void sendMessage(Object obj) throws IOException {
        sendMessage(JSON.toJSONString(obj));
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    public static void sendMessage(String shopName, Object result){
        try {
            CopyOnWriteArrayList<Session> list = map.get(shopName);
            if (list != null) {
                for (Session session : list) {
                    session.getBasicRemote().sendText(JSON.toJSONString(result));
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static void putSession(String shopName, Session session) {
        CopyOnWriteArrayList<Session> list = map.get(shopName);
        if (list == null) {
            list = new CopyOnWriteArrayList<>();
            list.add(session);
            map.put(shopName, list);
        } else {
            list.add(session);
        }
    }

    private static void removeSession(String shopName, Session session) {
        CopyOnWriteArrayList<Session> list = map.get(shopName);
        if (list == null) {
            throw new UnmessageException("没有这个session");
        } else {
            list.remove(session);
        }
    }


}
