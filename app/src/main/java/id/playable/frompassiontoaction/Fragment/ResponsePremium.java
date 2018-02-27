package id.playable.frompassiontoaction.Fragment;

import java.util.List;

import com.google.gson.annotations.SerializedName;


public class ResponsePremium{

	@SerializedName("msg")
	private String msg;

	@SerializedName("result")
	private boolean result;

	@SerializedName("data")
	private List<DataPremium> data;

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

	public void setData(List<DataPremium> data){
		this.data = data;
	}

	public List<DataPremium> getData(){
		return data;
	}

	@Override
 	public String toString(){
		return 
			"ResponsePremium{" + 
			"msg = '" + msg + '\'' + 
			",result = '" + result + '\'' + 
			",data = '" + data + '\'' + 
			"}";
		}
}