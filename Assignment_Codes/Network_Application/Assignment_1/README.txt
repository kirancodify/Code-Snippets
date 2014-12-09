ReadMe About Structured information Assignment
————————————————————————————————————————

Developer:

Kiran Kumar, student number: 399876, kiran.kumar@aalto.fi
Radhakrishnan Ilavarasi , student number: 400613, ilavarasi.radhakrishnan@aalto.fi


StructuredInformation_Eclipse_Project is a JAVA Eclipse project developed using:
Eclipse Java EE IDE for Web Developers.
Version: Helios Service Release 1
Build id: 20100917-0705

xmlValidateList is the .java file which can be complied and run from the command line with jre7 libraries

Files List
------------
- bookshelf.xsd → This is the required schema file that defines the schema of our bookshelf based on our bookshelf story in the document having various attributes related to the books in the shelf.

- xmlValidateList.java → Program contains the main() method where the execution begins whenever a user requests for an operations.The two operations:
				validate - validates the given xml file (as an argument in command line or exclipse window) against the schema (bookshelf.xsd). If the file is valid it displays the validity else If the file is invalid it also provides the reason for its invalidity;
				list - initially calls the validate method (operation explained above) and if its true then lists the books defined in the given xml file. 

- booklist.xml → sample file of book list according to the schema. This is not present as part of project but should be given as a command line arguement. Any other xml file can also be given to the code.

Procedure
-------------------
The code can be executed in 2 methods :

First Method :

1. Open the project in Eclipse IDE
2. Find the location of the xml file you need to validate (Eg: /Desktop/xxx.xml)
3. Open Run Configurations window in Eclipse and provide the arguments in the following usage format --- <filename with location> <operation/function> and run the code
4. Output can be seen in the console window

Second Method :

1. Open command line 
2. Check if all the files(booklist.xml,bookshelf.xsd and xmlValidateList.java) are the same directory.3. Compile the java program with the following command:javac xmlValidateList.java    4. Run the program: The required format is java <java filename> <xmlfilename> <option=validate or list>hence here the program can be run as java xmlValidateList New1.xml validate

