package com.simba.redis.util;

import org.springframework.stereotype.Component;
import java.io.*;
@Component
public class SerializeUtil {

    public static byte[] serialize(Object object){
        if(object == null){
            return  new byte[]{};
        }else{
            try{
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(object);
                return  bos.toByteArray();
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
    }
    public static Object  unserialize(byte[] bytes){
        if(bytes==null || bytes.length == 0){
            return  null;
        }else{
            try{
                ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                ObjectInputStream ois = new ObjectInputStream(bis);
                return  ois.readObject();
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
    }
}
