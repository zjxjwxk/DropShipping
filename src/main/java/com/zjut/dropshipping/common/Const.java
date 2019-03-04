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

        String TO_BE_CONFIRMED = "待确认";

        String TO_BE_RECEIVED = "待收货";

        String COMPLETED = "已完成";

        String REFUND = "退款";

        String REJECTED = "驳回";
    }

    public interface OrderModifyType {

        String COMPLETED = "completed";

        String CANCEL = "cancel";

        String REFUND = "refund";

        String RETURN = "return";
    }

    public interface RefundStatus {

        String REJECTED = "驳回";

        String REFUNDING = "退款中";

        String REFUND_SUCCESS = "退款成功";

        String REFUND_FAILED = "退款失败";
    }

    public interface AgreementResponse {

        String ACCEPT = "accept";

        String REFUSE = "refuse";
    }

    public interface UploadType {

        String IDENTITY = "IDCard";

        String IDENTITY_CARD_1 = "IDCard-1";

        String IDENTITY_CARD_2 = "IDCard-2";

        String GOODS = "goods";
    }

}
