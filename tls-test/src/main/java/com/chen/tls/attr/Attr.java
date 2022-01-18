package com.chen.tls.attr;

import io.netty.buffer.ByteBuf;
import io.netty.util.AttributeKey;

/**
 * @author chenwh
 * @date 2022/1/18
 */

public class Attr {

    public static AttributeKey<ByteBuf> BUFFER = AttributeKey.valueOf("buffer");
}
