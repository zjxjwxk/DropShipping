package com.zjut.dropshipping.dto;

import com.zjut.dropshipping.dataobject.Buyer;
import com.zjut.dropshipping.dataobject.Logistic;
import lombok.Data;

/**
 * @author zjxjwxk
 */
@Data
public class OrderDetailDTO {

    private Logistic logistic;
    private Buyer buyer;
}
