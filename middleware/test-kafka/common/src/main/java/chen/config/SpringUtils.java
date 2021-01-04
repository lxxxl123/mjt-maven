package chen.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring Bean工具类
 * @author echooymxq
 **/
@Component
public class SpringUtils implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	public static <T> T getBean(Class<T> beanClass) {
		return applicationContext.getBean(beanClass);
	}
	public static <T> T getBean(String beanName,Class<T> beanClass) {
		return applicationContext.getBean(beanName, beanClass);
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		SpringUtils.applicationContext = context;
	}


}
