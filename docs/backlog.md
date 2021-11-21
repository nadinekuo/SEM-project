## Backlog SEM group 25a

* This backlog is our "to-do-list" containing all user stories/requirements based on the scenario description for: Sports Centre
* All user stories are assigned a MOSCOW label: either "must", "should", "could", or "won't".
* All user stories are converted to GitLab Issues.

| MUST features | 
| --- | 
| Customers shall be able to register with a unique ID (username) and password, for either a basic or premium subscription. |
| The system shall have 1 default admin, who can create more admins with a unique ID (username) and password. | 
| Users (customers, admins) shall later be able to login the system with their unique ID (username) and password. |
| Customers shall be able to register for pre-allocated lesson slots (from 9:00 to 16:00). | 
| The system shall have 3 sport halls (X1, X2, X3) allowed for reservation by all users. | 
| Customers shall be able to make reservations (timeslot of 1 hour) for 1 selected sports hall/field, which is only available between 16:00 - 23:00.| 
| Customers shall be able to make reservations (time slot of 1 hour)  for (1 or more pieces of) selected, available equipment, which is only available between 16:00 - 23:00. | 
| For team sports, customers shall be able to make group reservations for the selected sport field/hall by entering all other group member’s IDs. |
| Group reservations for sport fields shall adhere to both the corresponding min. / max. capacity of related sport field, and min. required group size of sport it belongs to. | 
| Group reservations for sport halls shall adhere to the corresponding min. / max. capacity of the related sport hall and selected sport (since halls hold multiple sports). | 


| SHOULD features | 
| --- | 
| Customers having a basic subscription shall be able to upgrade their account to premium. |
| Customers shall be able to create a group/team with other users. | 
| For team sports, customers shall be able to make a group reservation (he/she should be part of that group) for the selected sport hall/field, without entering all group member IDs. |
|  Group bookings shall count towards every group member’s limit on reservations per day (limit depends on whether he/she has a premium or basic subscription). | 
| Admins shall be able to see who was the last user to book equipment (logs). | 


| COULD features | 
| --- | 
| Admins shall be able to add or remove sport fields and halls. | 
| Admins shall be able to adjust the details (capacity, name, etc) of a sport field and hall. | 
| Admins shall be able to add or remove equipment. | 
| Admins shall be able to add or remove sports. | 
| Admins shall be able to set a lesson capacity. |
| Admins shall be able to see event logs of when events such as users logging in and reservations have been made. | 
| Admins shall be able to cancel any reservation 6 hours in advance the latest. |
| Admins shall be able to add and remove lessons. | 
| Admins shall be able to change the time slots per reservation (default: 1 hour). | 
| Customers shall be able to cancel their own reservation. | 
| Customers shall be able to remove themselves from a group, but only if there are no reservations open. | 



| WON'T | 
| --- | 
| Admins shall be able to ban users who did not show up to a booked lesson. |
| Admins shall be able to ban users who did not return equipment on time. | 
| The system shall be extended with GUI. |


| Non-functional requirements | 
| --- | 
| The system shall be modular (can be extended with extra functionalities later). |
| Individual components of the system shall be scalable so that when the demand for that service increases, the service does not get overloaded (micro-services). | 
| The system shall allow for easy integration with other systems (API). | 
| The system shall be written in the Java programming language (version 11). | 
| All interactions with the systems shall be handled through the APIs. | 
| Passwords shall be stored safely using Spring Security. | 
| The system shall have a database to persist system data (H2 for development, Postgres for production). |
| The system shall have a minimum branch test coverage of 70%. | 