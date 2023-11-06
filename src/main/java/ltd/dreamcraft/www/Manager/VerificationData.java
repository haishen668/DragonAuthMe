package ltd.dreamcraft.www.Manager;

public class VerificationData {
    String code;
    String emailAddress;

    public VerificationData(String code, String emailAddress) {
        this.code = code;
        this.emailAddress = emailAddress;
    }

    public VerificationData(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
