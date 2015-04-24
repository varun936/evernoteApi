package notes;

import java.util.ArrayList;
import java.util.List;

import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.thrift.TException;

import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NoteController {

	@RequestMapping("/api/notes")
	public List<JSONObject> getNotes(@RequestParam(value="name", defaultValue="World") String name) throws Exception {
		Note note = new Note();
		List<com.evernote.edam.type.Note> notes = note.listNotes();
		List<JSONObject> resp = new ArrayList<>();
		for (com.evernote.edam.type.Note indNote : notes) {
			JSONObject noteJson = new JSONObject();
			noteJson.put("id",indNote.getGuid());
			noteJson.put("body", indNote.getTitle());
			resp.add(noteJson);
		}
		return resp;
	}


	@RequestMapping(value="/api/notes/{id}")
	public JSONObject getNote(@PathVariable("id") String id) throws TException, EDAMUserException, EDAMSystemException, EDAMNotFoundException {
		Note note = new Note();
		JSONObject noteJson = new JSONObject();

		com.evernote.edam.type.Note noteResp = note.getNote(id);
		noteJson.put("id", id);
		noteJson.put("body", noteResp.getTitle());
		return noteJson;
	}


	@RequestMapping(value="/api/notes", headers = {"content-type=application/json"}, method= RequestMethod.POST)
	public @ResponseBody JSONObject createNote(@RequestBody JSONObject body) throws TException, EDAMUserException, EDAMSystemException, EDAMNotFoundException {
		Note note = new Note();

		System.out.println(body);
		JSONObject noteJson = new JSONObject();

		com.evernote.edam.type.Note noteResp = note.createNote(body.toString());
		noteJson.put("id", noteResp.getGuid());
		noteJson.put("body", noteResp.getTitle());

		return noteJson;
	}
}
