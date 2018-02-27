package id.playable.frompassiontoaction.Model;

import java.util.List;

import com.google.gson.annotations.SerializedName;


public class ResponseComment{

	@SerializedName("msg")
	private String msg;

	@SerializedName("result")
	private boolean result;

	@SerializedName("data")
	private List<CommentModel> data;

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

	public void setData(List<CommentModel> data){
		this.data = data;
	}

	public List<CommentModel> getData(){
		return data;
	}

	@Override
 	public String toString(){
		return 
			"ResponseComment{" + 
			"msg = '" + msg + '\'' + 
			",result = '" + result + '\'' + 
			",data = '" + data + '\'' + 
			"}";
		}
}