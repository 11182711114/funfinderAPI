
public class User{
    
    private int age;
    private String lastName, password, email, name;

    public User(String name, String lastName, String password, String email, int age){
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.age = age;
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public String getLastName(){
        return lastName;
    }

    public String getPassword(){
        return password;
    }

    public String getEmail(){
        return email;
    }

    public int getAge(){
        return age;
    }
    
}