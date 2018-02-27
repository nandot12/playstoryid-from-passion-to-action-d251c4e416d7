package id.playable.frompassiontoaction.Model;


import com.google.gson.annotations.SerializedName;


public class ResponseInsert{

	@SerializedName("msg")
	private String msg;

	@SerializedName("result")
	private boolean result;

	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}

	public void setResult(boolean result){
		this.result = result;
	}

	public boolean isResult(){
		return result;
	}

	@Override
 	public String toString(){
		return 
			"ResponseInsert{" + 
			"msg = '" + msg + '\'' + 
			",result = '" + result + '\'' + 
			"}";
		}
}