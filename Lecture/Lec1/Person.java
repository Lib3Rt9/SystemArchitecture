import java.io.*;
public class Person implements Serializable {
	String firstname;
	String lastname;
	int age;
	public Person(String firstname, String lastname, int age) {
	    this.firstname = firstname;
	    this.lastname = lastname;
	    this.age = age;
	}
	public String toString() {
	    return this.firstname+" "+this.lastname+" "+this.age;
        }
}
