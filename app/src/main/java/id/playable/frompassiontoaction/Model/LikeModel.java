package id.playable.frompassiontoaction.Model;


import com.google.gson.annotations.SerializedName;


public class LikeModel {

	@SerializedName("like_id")
	private String likeId;

	@SerializedName("like_buku")
	private String likeBuku;

	@SerializedName("like_count")
	private String likeCount;

	@SerializedName("dislike_count")
	private String dislikeCount;

	public void setLikeId(String likeId){
		this.likeId = likeId;
	}

	public String getLikeId(){
		return likeId;
	}

	public void setLikeBuku(String likeBuku){
		this.likeBuku = likeBuku;
	}

	public String getLikeBuku(){
		return likeBuku;
	}

	public void setLikeCount(String likeCount){
		this.likeCount = likeCount;
	}

	public String getLikeCount(){
		return likeCount;
	}

	public void setDislikeCount(String dislikeCount){
		this.dislikeCount = dislikeCount;
	}

	public String getDislikeCount(){
		return dislikeCount;
	}

	@Override
 	public String toString(){
		return 
			"LikeModel{" +
			"like_id = '" + likeId + '\'' + 
			",like_buku = '" + likeBuku + '\'' + 
			",like_count = '" + likeCount + '\'' + 
			",dislike_count = '" + dislikeCount + '\'' + 
			"}";
		}
}