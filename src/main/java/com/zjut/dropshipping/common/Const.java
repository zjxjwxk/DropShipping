package com.zjut.dropshipping.common;

/**
 * 常量类
 * @author zjxjwxk
 */
public class Const {

    public static final String CURRENT_AGENT = "currentAgent";
    public static final String CURRENT_PRODUCER = "currentProducer";
  
    public static final String PRODUCERTOAGENT = "1";
    
    public static final String PHONE = "phone";
    public static final String IDENTITY_NUMBER = "identityNumber";
    public static final String EXTERNAL_SHOP = "externalShop";

    public static final String PRODUCER_NAME = "producerName";
    public static final String LICENSE_NUMBER = "licenseNumber";

    public interface AccountState {

        String NORMAL = "正常";

        String UNREVIEWED = "未审核";

        String FROZEN = "冻结";
    }

    public interface AgreementState {

        String NORMAL = "正常";

        String AGENT_REQUEST = "代理发送请求";

        String PRODUCER_REQUEST = "厂商发送请求";
    }

    public interface OrderState {

        String NORMAL = "正常";
    }

    public interface AgreementResponse {

        String ACCEPT = "accept";

        String REFUSE = "refuse";
    }

    public interface UploadType {

        String IDENTITY = "IDCard";

        String IDENTITY_CARD_1 = "IDCard-1";

        String IDENTITY_CARD_2 = "IDCard-2";
    }

}
