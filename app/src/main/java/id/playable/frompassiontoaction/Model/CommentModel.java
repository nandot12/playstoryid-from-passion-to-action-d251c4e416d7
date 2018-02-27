package id.playable.frompassiontoaction.Model;


import com.google.gson.annotations.SerializedName;


public class CommentModel {

	@SerializedName("comment_text")
	private String commentText;

	@SerializedName("comment_name")
	private String commentName;

	public String getCommentImage() {
		return commentImage;
	}

	public void setCommentImage(String commentImage) {
		this.commentImage = commentImage;
	}

	@SerializedName("comment_image")
	private String commentImage;
	@SerializedName("comment_tanggal")
	private String commentTanggal;

	@SerializedName("comment_id")
	private String commentId;

	public void setCommentText(String commentText){
		this.commentText = commentText;
	}

	public String getCommentText(){
		return commentText;
	}

	public void setCommentName(String commentName){
		this.commentName = commentName;
	}

	public String getCommentName(){
		return commentName;
	}

	public void setCommentTanggal(String commentTanggal){
		this.commentTanggal = commentTanggal;
	}

	public String getCommentTanggal(){
		return commentTanggal;
	}

	public void setCommentId(String commentId){
		this.commentId = commentId;
	}

	public String getCommentId(){
		return commentId;
	}

	@Override
 	public String toString(){
		return 
			"Data{" +
			"comment_text = '" + commentText + '\'' + 
			",comment_name = '" + commentName + '\'' + 
			",comment_tanggal = '" + commentTanggal + '\'' + 
			",comment_id = '" + commentId + '\'' + 
			"}";
		}
}