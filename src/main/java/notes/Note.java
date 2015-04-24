package notes;

public class Note {
	private final long id;
	private final String body;

	public Note(long id, String body) {
		this.id = id;
		this.body = body;
	}

	public long getId() {
		return id;
	}

	public String getBody() {
		return body;
	}

}
