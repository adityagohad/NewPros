package pros.app.com.pros.profile.model;

public class ContactsModel {

    String name;
    String phoneNumber;
    boolean isSelected;

    public ContactsModel(String name, String phoneNumber, boolean isSelected) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
