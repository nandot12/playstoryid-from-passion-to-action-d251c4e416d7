package id.playable.frompassiontoaction.Fragment;


import com.google.gson.annotations.SerializedName;


public class DataPremium {

	@SerializedName("user_id")
	private Object userId;

	@SerializedName("premium_code")
	private String premiumCode;

	public void setUserId(Object userId){
		this.userId = userId;
	}

	public Object getUserId(){
		return userId;
	}

	public void setPremiumCode(String premiumCode){
		this.premiumCode = premiumCode;
	}

	public String getPremiumCode(){
		return premiumCode;
	}

	@Override
 	public String toString(){
		return 
			"DataPremium{" +
			"user_id = '" + userId + '\'' + 
			",premium_code = '" + premiumCode + '\'' + 
			"}";
		}
}