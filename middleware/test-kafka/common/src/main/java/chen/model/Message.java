package chen.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author chenwh
 * @date 2021/1/4
 */

@Data
public class Message  {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String message;

    public Message(){}
}
