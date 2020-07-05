package top.youlanqiang.orderproject;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;

import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;



@SpringBootApplication
@EnableSwagger2
@EnableTransactionManagement
@MapperScan("top.youlanqiang.orderproject.*.mapper")
public class OrderprojectApplication {



	@Bean
	public HttpMessageConverters fastJsonConfigure(){
		FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(
				SerializerFeature.PrettyFormat,
				SerializerFeature.WriteNullNumberAsZero,
				SerializerFeature.WriteNullListAsEmpty,
				SerializerFeature.WriteNullStringAsEmpty,
				SerializerFeature.WriteNullBooleanAsFalse);
		//日期格式化
		fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
		converter.setFastJsonConfig(fastJsonConfig);
		return new HttpMessageConverters(converter);
	}

	public static void main(String[] args) {

	  	SpringApplication.run(OrderprojectApplication.class, args);

	}

}
