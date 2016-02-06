 - We used a SQL database to store each website's URL, HTML, and TEXT that the crawler finds.
	- Therefore, running our code requires some modification to the source files -- mainly setting up a database
	  (we've created a createtable.sql file to assist you) and modifying sql login credentials.
 - The crawler's text get method sometimes fails to separate each word.
 - We used an semi-automated java file to analyze the results from our database cleverly named Analyze.java.
	It was used to answer most of the questions in answers.txt
