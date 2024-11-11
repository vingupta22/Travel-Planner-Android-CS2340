package model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.CS2340FAC_Team41.R;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private List<Note> notesList;

    /**
     * Constructs a NotesAdapter with the specified list of notes.
     *
     * @param notesList the list of notes to be managed by the adapter
     */
    public NotesAdapter(List<Note> notesList) {
        this.notesList = notesList;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notesList.get(position);
        holder.noteContent.setText(note.getContent());
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView noteContent;

        /**
         * Initializes the NoteViewHolder with the specified item view and its UI components.
         *
         * @param itemView the view representing a single note item
         */
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteContent = itemView.findViewById(R.id.note_content);
        }
    }
}

