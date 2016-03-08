package com.ddosd.facade.entity;
import javax.persistence.CascadeType; 
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import com.evalua.entity.support.EntityBase;

@Entity
public class FileAttachment extends EntityBase{
	
	public enum Status{
		ACTIVE,DELETED;
	}
	
	public enum Category{
		BOOK,DOCUMENT,SPORTS,PROGRAMS,COLLECTION,OTHER;
	}
	
	
	private String name;
	private String hash;
	private String extension;

	private String mimeType;
	private int size;
	private byte[] attachment = new byte[0];
	private byte[] ori = new byte[0];
	private Boolean original = true;
	private FileAttachment originalReference;
	private String tag;	
	private Status status=Status.ACTIVE;
	private User user;
	private String description;
	private User sharedBy;
	private Category category=Category.DOCUMENT;
	private boolean valid;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	@Column(length = 5000000)
	public byte[] getAttachment() {
		return attachment;
	}

	public void setAttachment(byte[] attachment) {
		this.attachment = attachment;
	}
	
	@Column(length = 5000000)
	public byte[] getOri() {
		return ori;
	}

	public void setOri(byte[] ori) {
		this.ori = ori;
	}

	public Boolean getOriginal() {
		return original;
	}

	public void setOriginal(Boolean original) {
		this.original = original;
	}
	
	@OneToOne(cascade = CascadeType.REFRESH)
	public FileAttachment getOriginalReference() {
		return originalReference;
	}

	public void setOriginalReference(FileAttachment originalReference) {
		this.originalReference = originalReference;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	@OneToOne(cascade = CascadeType.REFRESH)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToOne(cascade = CascadeType.REFRESH)
	public User getSharedBy() {
		return sharedBy;
	}

	public void setSharedBy(User sharedBy) {
		this.sharedBy = sharedBy;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	
}
