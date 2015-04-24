package notes;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NoteController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();


	@RequestMapping("/api/notes")
	public List<com.evernote.edam.type.Note> getNotes(@RequestParam(value="name", defaultValue="World") String name) throws Exception {

		Note note = new Note();

		List<com.evernote.edam.type.Note> notes = note.listNotes();
//		JSONObject obj = new JSONObject();
		return notes;
	}
}
