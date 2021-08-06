package com.chen;

import com.dianping.cat.Cat;
import com.dianping.cat.message.internal.MessageIdFactory;
import com.dianping.cat.message.spi.MessageTree;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.UUID;

@Slf4j
public class CatServiceLogUtils {

  private static final MessageIdFactory CAT_MESSAGEID_FACTORY = new MessageIdFactory();
  static {
    try {
      Cat.enableMultiInstances();
      String s = UUID.randomUUID().toString().replace("-", "");
      CAT_MESSAGEID_FACTORY.initialize(s);
    } catch (Exception e) {
      log.error("初始化CAT ID生成器异常", e);
    }
  }


  public static final String KEY_CHILD = "X-CAT-ID";
  public static final String KEY_PARENT = "X-CAT-PARENT-ID";
  public static final String KEY_ROOT = "X-CAT-ROOT-ID";

  private static final String TRACE_ID = "traceId";

  public static String initTraceId() {
    String messageId = gainMessageId();
    if (MDC.get(TRACE_ID) == null) {
      MDC.put(TRACE_ID, messageId);
      return messageId;
    }
    return "";
  }

  public static void clearTraceId(){
    MDC.remove(TRACE_ID);
  }


  public static void clearTraceId(String id) {
    if (!id.isEmpty() && id.equals(MDC.get(TRACE_ID))) {
      MDC.remove(TRACE_ID); 
    }
  }

  private static String gainMessageId() {
    try {
      MessageTree tree = Cat.getManager().getThreadLocalMessageTree();
      if (tree == null) {
        return CAT_MESSAGEID_FACTORY.getNextId();
      }
      if (tree.getRootMessageId() != null) {
        return tree.getRootMessageId();
      }
      String messageId = tree.getMessageId();
      if (messageId == null) {
        messageId = Cat.createMessageId();
        tree.setMessageId(messageId);
      }
      return messageId;
    } catch (Exception ignore) {
      log.error("获取CAT的MESSAGEID异常", ignore);
      return "UNKNOW";
    }
  }



}