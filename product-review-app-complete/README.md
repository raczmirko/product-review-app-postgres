# About the project
Product Review App Complete <b> was inspired by </b> my habit of reviewing products for myself to better understand my habits. In fact, I wrote my <b> BSc diploma work </b> about designing a product rating database server. This is the underlying database structure that I am using for this application. I also wanted to build a complex project to learn <b> React </b> and to showcase my skills in <i> Spring, relational database management systems and Java </i>.
## Underlying database structure functionality
The system in question allows for the evaluation of products based on various criteria, such as taste, packaging, color, etc. Products can be food items, consumer goods, electronics, and so on.

The system maintains records of conceptual products, i.e., items that can be purchased as specific products in various packaging options at stores. Each item has a brand and can be categorized, and the categories can be hierarchically organized (main category and subcategories) up to a depth of 3. For example, a main category could be beverages, with its subcategories being tea or coffee. Each category has characteristics (e.g., color, taste), and products belonging to that category can have these characteristics. Additionally, every category can be evaluated based on certain criteria (e.g., taste for the beverage category). Products belonging to a given category can be rated on a scale from 1 to 5 as part of the product reviews, based on the criteria specific to the category, as well as inherited criteria from higher-level categories. Product images can also be stored for each product. Each evaluation comes from an individual and pertains to a specific product. Every data modification is logged in the system.
# Configuration
## Microsoft SQL Server
### Database creation
The full database creation script is located in the <i> src/resources/db/create_database.sql </i> file. You have to manually run this <b> before </b> running the application!
> [!IMPORTANT]
> You will need to have Microsoft SQL Server 16 or higher installed on your machine, as well as SQl Server Management Studio (SSMS). These are <b> only available on Windows. </b>
> If you want to use your own password and username, you can modify this script, and then you <b> have to modify the application.properties file too </b> to add the new connection String to the database server!
### SQL Server Network Configuration
Since the application is a separate entity from the database server running on our machine, SQL Server has to be configured to enable the TCP/IP protocol to enable communications with the DB over the network.
The easiest way of configuration is the graphical SQL Server Configuration Manager:
1. Type "Configuration Manager" in the Windows search bar and you should see and option like <b> SQL Server [version] Configuration Manager </b>.
2. Open the Configuration Manager and expand the SQL Server Network Configuration item in the left-hand menu.
3. Click on the Protocols for [serverName] sub-item.
4. On the right-hand side double click in the TCP/IP option (status should show Disabled at this point).
5. In the popup modal, set the Enabled option to Yes. Click OK.
6. SQL Server has to be restarted for the changes to take effect. Either restart your whole PC or just go the the SQL Server Services option in left-hand menu and right click/restart on the server.

> [!IMPORTANT]
> To get more information about configuring the SQL Server Network protocols read the official documentation [here](https://learn.microsoft.com/en-us/sql/database-engine/configure-windows/enable-or-disable-a-server-network-protocol).
### Authentiction mode
> [!WARNING]
> The database server's authentication mode must be changed from 'Windows 
> Authentication Mode' to 'SQL Server and Windows Authentication Mode'. 
> After changing, SQL Server must be restarted!

To change the authentication mode, follow the steps below:
- In the <i>Object Explorer</i> right click on the Server, and then click 
  <i>properties</i>.
- Go to the Security tab!
- Change the Server Authentication radio button!

## application.properties
You will have to change the <serverName> in the <i> application.properties </i> to match your SQL Server's name. If you open SSMS to join to the server you will see what the server's name is set to, usually it is the name of the PC the server is running on.
> [!IMPORTANT]
> You must remove the brackets (<>) from around the server name!
 
