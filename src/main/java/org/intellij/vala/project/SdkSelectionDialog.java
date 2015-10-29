package org.intellij.vala.project;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.vfs.VirtualFile;
import org.intellij.vala.project.template.ValaFilesChooserDescriptor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class SdkSelectionDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField sdkHomePath;
    private JButton browseButton;
    private JLabel errorLabel;
    private JRadioButton gtk3RadioButton;
    private JRadioButton gtk2RadioButton;
    private ValaSdkDescriptor selectedSdk;

    public SdkSelectionDialog(JComponent parent) {
        super((Window) parent.getTopLevelAncestor());
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        browseButton.addActionListener(e -> onBrowse());

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onBrowse() {
        VirtualFile valaDir = FileChooser.chooseFile(new ValaFilesChooserDescriptor(), this, null, null);
        sdkHomePath.setText(valaDir.getCanonicalPath());
    }

    private void onOK() {
        try {
            selectedSdk = ValaSdkDescriptor.fromDirectory(new File(sdkHomePath.getText()), getGtkVersion());
            dispose();
        } catch (IOException e) {
            errorLabel.setText("Could not create Vala SDK: " + e.getMessage());
        }
    }

    private Optional<ValaSdkDescriptor.GtkVersion> getGtkVersion() {
        if (gtk3RadioButton.isSelected()) {
            return Optional.of(ValaSdkDescriptor.GtkVersion.GTK_3);
        } else if (gtk2RadioButton.isSelected()) {
            return Optional.of(ValaSdkDescriptor.GtkVersion.GTK_2);
        } else {
            return Optional.empty();
        }
    }

    private void onCancel() {
        selectedSdk = null;
        dispose();
    }

    public ValaSdkDescriptor open() {
        this.pack();
        this.setVisible(true);
        return selectedSdk;
    }
}
