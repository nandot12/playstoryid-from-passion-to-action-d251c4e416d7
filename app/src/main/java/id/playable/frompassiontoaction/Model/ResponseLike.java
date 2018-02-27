package id.playable.frompassiontoaction.Model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ResponseLike{

	@SerializedName("msg")
	private String msg;

	@SerializedName("result")
	private boolean result;

	@SerializedName("data")
	private List<LikeModel> data;

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

	public void setData(List<LikeModel> data){
		this.data = data;
	}

	public List<LikeModel> getData(){
		return data;
	}

	@Override
 	public String toString(){
		return 
			"ResponseLike{" + 
			"msg = '" + msg + '\'' + 
			",result = '" + result + '\'' + 
			",data = '" + data + '\'' + 
			"}";
		}
}