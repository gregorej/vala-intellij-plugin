using Gtk;

public class SearchDialog : Dialog {

    private Entry search_entry;
    private CheckButton match_case;
    private CheckButton find_backwards;
    private Widget find_button;

    public signal void find_next (string text, bool case_sensitivity);
    public signal void find_previous (string text, bool case_sensitivity);

    public SearchDialog () {
        this.title = "Find";
        this.border_width = 5;
        set_default_size (350, 100);
        create_widgets ();
        connect_signals ();
    }

    private void create_widgets () {

        // Create and setup widgets
        this.search_entry = new Entry ();
        var search_label = new Label.with_mnemonic ("_Search for:");
        search_label.mnemonic_widget = this.search_entry;
        this.match_case = new CheckButton.with_mnemonic ("_Match case");
        this.find_backwards = new CheckButton.with_mnemonic ("Find _backwards");

        // Layout widgets
        var hbox = new Box (Orientation.HORIZONTAL, 20);
        hbox.pack_start (search_label, false, true, 0);
        hbox.pack_start (this.search_entry, true, true, 0);
        var content = get_content_area () as Box;
        content.pack_start (hbox, false, true, 0);
        content.pack_start (this.match_case, false, true, 0);
        content.pack_start (this.find_backwards, false, true, 0);
        content.spacing = 10;

        // Add buttons to button area at the bottom
        add_button (Stock.HELP, ResponseType.HELP);
        add_button (Stock.CLOSE, ResponseType.CLOSE);
        this.find_button = add_button (Stock.FIND, ResponseType.APPLY);
        this.find_button.sensitive = false;

        show_all ();
    }

    private void connect_signals () {
        this.search_entry.changed.connect (() => {
            this.find_button.sensitive = (this.search_entry.text != "");
        });
        this.response.connect (on_response);
    }

    private void on_response (Dialog source, int response_id) {
        switch (response_id) {
        case ResponseType.HELP:
            // show_help ();
            break;
        case ResponseType.APPLY:
            on_find_clicked ();
            break;
        case ResponseType.CLOSE:
            destroy ();
            break;
        }
    }

    private void on_find_clicked () {
        string text = this.search_entry.text;
        bool cs = this.match_case.active;
        if (this.find_backwards.active) {
            find_previous (text, cs);
        } else {
            find_next (text, cs);
        }
    }
}

int main (string[] args) {
    Gtk.init (ref args);
    var dialog = new SearchDialog ();
    dialog.destroy.connect (Gtk.main_quit);
    dialog.show ();
    Gtk.main ();
    return 0;
}