# Airport-system-backend

In this project, we design a plane ticket reservation system, whose features are listed below. The whole program runs in the console and there is
no graphical part.You are probably familiar with Alibaba or Flitive. In this project, like travel agencies, the user (passenger) enters the details of the trip
in the system, (for example, the date of travel, the airline in question, etc.), then the system shows him the flights with capacity according to the user's
input, and finally The user can purchase one of the tickets or not. If a ticket is purchased, one of the capacity of that flight must be reduced.

"The overall structure of the project"

The overall structure of program's packages and classes is as follows:

com.math.ap.winter2022

--> App.class

--> model

  -->Airline.class    

  -->User.class

  -->Flight.class

  -->Passenger.class

--> server

  --> connection

    --> Connection.class

    --> ServerConnection.class

  --> Controller.class

  --> Server.class

The content of each class will be explained below.

- In this project we will have two user models. Airlines and passengers.
Airlines are the ones who enter the flights into the system. In fact, the data (which will be later filtered and shown to passengers) is obtained through these users.
Passengers are those who enter the site to purchase a ticket.

- Registration and login
The commands related to registration and logging are implemented in the Controller class located in the server package by the functions register and login.

- Definition of flights and tickets by airlines
Airlines can add flights to the flight system.
The ticket insertion command is implemented in the insertFlight function located in the Controller, which is as follows: 
"public void insertFlight(User airline, String command)"

Notes:
The command string contains the parameters required to create the flight ticket, separated by spaces. The format of this field is as follows:
[flight number: int] [from: String] [to: String] [date :dd-mm-yyyy] [time: hh:mm] [ticketPrice : long] [capacity: int]

An example of the command is as follows:
"737 tehran kyiv 08-01-2020 06:12 16000000 167"

It should throw an exception in the following situations:

If the flight number has already been entered.

If the input had too few/too many parameters or the numeric format of one of its inputs was invalid 
(for example, a non-numeric string was entered instead of the flight number or number of passengers or ticket price).

If the logged in user was not an airline type.

If there was a problem with the given date and time format 
(for example, they were not in the specified format, or a number greater than 12 was entered instead of the month, or a value greater than 30 was entered instead of the day number,
or the hours were not valid, for example, 25:30 or 12:64 are not valid) or the various parts of the date (month year hour and minute) were not in numeric format.

- Client/server architecture

In this section, we are going to implement this architecture with data streams (streams). Streams have the ability to send data one-way,
and to use them in the client-server architecture, there must be two streams (one for the flow of inputs and another for the flow of outputs).
The server will play the role of a system that users (clients) can communicate with by sending requests from one stream and receiving responses from another
stream. We explain the details of this implementation below:

"connection package"
In this package, two classes Connection and ServerConnection (which inherits from Connection) are placed.

Connection:
This class has two streams, one to receive inputs and the other to send outputs. In the constructor of this class, we create two objects of type Scanner 
(using inputstream) and PrintWriter (using outputstream) (these two objects are among the fields of the class).

ServerConnection:
In this class, similar to the Connection class, we have two input and output streams, whose function is similar to Connection. 
In addition to these two streams, we also have an InputStream called notificationInputStream, which is used to receive notifications sent by the client.
Using this stream, nextNotification function is written in this class so that it receives and returns the notifications from the next line in the notificationInputStream.

- Server class
The connectToServer function in this class returns a ServerConnection object. To receive user requests, send responses, and send notifications, we need
streams in the ServerConnection object to be connected to streams on the opposite side so that we can read requests, respond, and send notifications with them.
So for each InputStream in this object, we have created an OutputStream that is connected to it so that we can send messages to the user who uses this object,
and similarly we need an InputStream to read the requests that the user sends to our OutputStream, which is connected to the stream of user requests.

- Commands related to streams
The way the program that we have written so far works is that each client receives a ServerConnection object by calling the Server.connectToServer() function
and then it can write its requests to the OutputStream and read the responses from the InputStream. and also reading notifications) communicate with the server.
In the following, we explain the commands that the client can use to communicate with the server and how to respond to them:

Note: note that in all sections we have the default output >> which indicates the end of the output related to that command.

Register command:
The structure of this command is as follows:
"register [username] [password]"
Whenever the client sends this command to the server, the server performs the corresponding operation using the function implemented in the Conroller.
If any error occurs, an error message is sent to the client.

Login command:
The structure of this command is as follows:
"login [username] [password]"
Whenever the client sends this command to the server, the server performs the corresponding operation using the function implemented in the Conroller.
If any error occurs, an error message! It is sent to the client.

Search command:
The structure of this command is such that first the keyword search, then the rest of the filters come with a space, so that in each filter,
its type is first, then the colon sign, and then its input:
"search [filter1:value1] [filter2:value2] .."
Clients (which are of passenger type) can find their desired tickets with the search command by applying a number of filters.

The user can enter the following items as a filter in the search command:

To determine the day of departure: departure
The date the passenger wants to book a flight on that day in dd-mm-yyyy format

The name of the airline
A string representing the name of the airline

To specify the origin: from
A string consisting of letters and numbers without spaces

To specify the destination: to
A string consisting of letters and numbers without spaces

To specify the price range: price_range
Two non-negative numbers separated by a dash (dash:-).

Example:
search departure:01-02-2022 airline:pegasus from:tehran to:frankfurt price_range:0-1234567890

Notes:
As you can see, the filters do not have any special order and the user can apply the filters in any desired order, for example the following command is exactly the same as the above command:
search airline:pegasus price_range:0-1234567890 departure:01-02-2022 from:tehran to:frankfurt

User may use one or all filters.

If no filter was used and only the search command was entered, you should show all available tickets to the user.

It is guaranteed that the user will enter the command correctly and there will be no mistakes in the date, time, etc.

After a user uses the search command, we show him the search result as described below.

If there are no results matching the filters, we only have the same default output <<.
Otherwise, we will show the user all the flights that match the entered filter, whether they are full or have capacity. In this way, the specifications of
each ticket (all 10 fields of the Flight class constructor) are shown in one line, and if the capacity is full, full capacity is written in front of it.
Finally, the obtained result is sent to the client.

- ticket purchase
We implement the ticket buying function in the Controller class as follows:
public void purchase(User user, int flightNo, int n)

Inputs:
Buyer user
Flight number flightNo
Number of tickets n

This function buys n tickets from the flight with the number flightNo for the given user.

- Reservations menu for passengers
Clients can receive the list of purchased tickets from the server by sending the reservations command to the server. The tickets are displayed in the
same way as in the search, with the difference that the number of tickets is displayed at the end with the keyword number.

If a ticket was not booked by the user, we only have the default >> output.

Example of the output received from the server:

airline:qatar_airways flight_no:3345 from:tehran to:dubai departure_date:04-10-2022 departure_time:13:00 price:13000000 available_seats:165 number:3
>>

- Notification system
In this section, we implement the notification feature. We consider the last search performed by the user, every 100 milliseconds we print the number of
flights with capacity that apply to the filters of that search in one line, in the stream where the ServerConnection.notifactionScanner of that particular
user is opened; If the user has not searched until now, we will not post any output on this stream.

