package notes;

import java.util.List;

import com.evernote.auth.EvernoteAuth;
import com.evernote.auth.EvernoteService;
import com.evernote.clients.ClientFactory;
import com.evernote.clients.NoteStoreClient;
import com.evernote.clients.UserStoreClient;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.NoteSortOrder;
import com.evernote.edam.type.Notebook;
import com.evernote.thrift.TException;

public class Note {
	private static final String developerToken = "S=s1:U=90bb4:E=15441b12eea:C=14cea0000b8:P=1cd:A=en-devtoken:V=2:H=3c703968dba737902da41dc9d81e3a04";

	private  long id;
	private  String body;

	private UserStoreClient userStore;
	private NoteStoreClient noteStore;
	private String newNoteGuid;

	public Note() throws TException, EDAMSystemException, EDAMUserException {
//		this.id = id;
//		this.body = body;
		// Set up the UserStore client and check that we can speak to the server
		EvernoteAuth evernoteAuth = new EvernoteAuth(EvernoteService.SANDBOX, developerToken);
		ClientFactory factory = new ClientFactory(evernoteAuth);
		userStore = factory.createUserStoreClient();

		boolean versionOk = userStore.checkVersion("Evernote EDAMDemo (Java)",
				com.evernote.edam.userstore.Constants.EDAM_VERSION_MAJOR,
				com.evernote.edam.userstore.Constants.EDAM_VERSION_MINOR);
		if (!versionOk) {
			System.err.println("Incompatible Evernote client protocol version");
			System.exit(1);
		}

		// Set up the NoteStore client
		noteStore = factory.createNoteStoreClient();

	}

	public void setId(Long id) {
		 this.id= id;
	}

	public long getId() {
		return id;
	}

	public void setBody(String body) {
		this.body= body;
	}

	public String getBody() {
		return body;
	}


	public List<com.evernote.edam.type.Note> listNotes() throws Exception {
		List<Notebook> notebooks = noteStore.listNotebooks();
		List<com.evernote.edam.type.Note> notes =null;

		for (Notebook notebook : notebooks) {
			NoteFilter filter = new NoteFilter();
			filter.setNotebookGuid(notebook.getGuid());
			filter.setOrder(NoteSortOrder.CREATED.getValue());
			filter.setAscending(true);

			NoteList noteList = noteStore.findNotes(filter, 0, 100);
			notes = noteList.getNotes();
		}
		return notes;
	}

	public com.evernote.edam.type.Note getNote(String guid) throws EDAMUserException, EDAMSystemException, TException, EDAMNotFoundException {
		com.evernote.edam.type.Note note = noteStore.getNote(guid, true, true, false, false);
		return note;
	}

	public com.evernote.edam.type.Note createNote(String userNote) throws EDAMUserException, EDAMSystemException, TException, EDAMNotFoundException {
		com.evernote.edam.type.Note note = new com.evernote.edam.type.Note();
		note.setTitle(userNote);
		com.evernote.edam.type.Note createdNote = noteStore.createNote(note);
		newNoteGuid = createdNote.getGuid();
		return createdNote;
	}

}
