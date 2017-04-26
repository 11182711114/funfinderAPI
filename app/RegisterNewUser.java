
public class RegisterNewUser{
    /*Denna klass ska ta emot data för att skapa en ny user
    Klassen ska även kontrollera variablerna för att se till att
    de är ifyllda. Email är unik och ska kontrolleras i databasen innan
    en ny användaren registreras*/
    
    private String name, lastName, email, password;
    private int age;
    
    /*null och 0 ska ersättas med de fält som används vid registreringssidan.*/
    public void registerNewUser(){
        name = null;
        lastName = null;
        email = null;
        password = null;
        age = 0;
    
        if(!(name.isEmpty() || lastName.isEmpty() || email.isEmpty()|| password.isEmpty())){
            if(true/* <---!Database.contains(email)*/){
                 User u = new User(name, lastName, email, password, age);   
            } else {
                //echo "Email is already in use!";
            }
        }
    }
    
    
}