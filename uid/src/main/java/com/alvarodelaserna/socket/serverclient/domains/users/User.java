package com.alvarodelaserna.socket.serverclient.domains.users;

public class User {

	public final String email;
	public final String phoneNumber;
	public final String uuid;
	public String avatarId;
	public final String publicKey;
	public final boolean validated_au10tix;
	public final boolean validated_directid;
	private final Object directIdData;

	private User(Builder builder) {
		this.phoneNumber = builder.phoneNumber;
		this.email = builder.email;
		this.uuid = builder.uuid;
		this.avatarId = builder.avatarId;
		this.publicKey = builder.publicKey;
		this.directIdData = builder.directIdData;
		this.validated_au10tix = builder.validated_au10tix;
		this.validated_directid = builder.validated_directid;
	}

	@SuppressWarnings("UnusedReturnValue")
	public static class Builder {

		private String email;
		private String phoneNumber;
		private String uuid;
		private String avatarId;
		private String publicKey;
		private Object directIdData;
		private boolean validated_au10tix;
		private boolean validated_directid;

		public User build() {
			return new User(this);
		}

		public User.Builder toBuild(User userEncrypted) {
			this.email = userEncrypted.email;
			this.phoneNumber = userEncrypted.phoneNumber;
			this.uuid = userEncrypted.uuid;
			this.avatarId = userEncrypted.avatarId;
			this.publicKey = userEncrypted.publicKey;
			this.directIdData = userEncrypted.directIdData;
			this.validated_au10tix = userEncrypted.validated_au10tix;
			this.validated_directid = userEncrypted.validated_directid;
			return this;
		}

		public User.Builder email(String email) {
			this.email = email;
			return this;
		}

		public User.Builder phoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
			return this;
		}

		public User.Builder uuid(String uuid) {
			this.uuid = uuid;
			return this;
		}

		public User.Builder avatar(String avatar) {
			this.avatarId = avatar;
			return this;
		}

		public User.Builder publicKey(String publicKey) {
			this.publicKey = publicKey;
			return this;
		}

		public User.Builder directIdData(Object directIdEncripted) {
			this.directIdData = directIdEncripted;
			return this;
		}


		public User.Builder validatedAu10tix(boolean validated_au10tix) {
			this.validated_au10tix = validated_au10tix;
			return this;
		}

		public User.Builder validatedDirectId(boolean validated_directid) {
			this.validated_directid = validated_directid;
			return this;
		}
	}
}
