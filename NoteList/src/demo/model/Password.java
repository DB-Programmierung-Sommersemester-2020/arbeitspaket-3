package demo.model;

public class Password {
	private int id;
	private byte[] salt;
	private byte[] PasswordHash;
	
	public Password(int id, byte[] salt, byte[] passwordHash) {
		super();
		this.id = id;
		this.salt = salt;
		PasswordHash = passwordHash;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte[] getSalt() {
		return salt;
	}

	public void setSalt(byte[] salt) {
		this.salt = salt;
	}

	public byte[] getPasswordHash() {
		return PasswordHash;
	}

	public void setPasswordHash(byte[] passwordHash) {
		PasswordHash = passwordHash;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Password other = (Password) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
}
