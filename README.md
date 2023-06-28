# r5StaffChat
Minecraft plugin - chat visible globally among instances of servers for staff members

# Installation

1. Put jar file into plugins directory on all of your servers running Paper/Purpur version 1.19.4 or higher.
2. Restart server.
3. Go to plugins/r5StaffChat and open config.yml.
4. Customize for your server and plugins configuration:
   - change redis host if it's running on another IP
   - set server name - change 'Unknown Server' and set 'override-name' to true
   - add or remove ranks
   - do not forget to apply permissions for ranks/groups to your permissions plugin
5. Reload plugin (/r5chat reload) or restart server if Redis does not run on 127.0.0.1.

# Commands

/r5chat reload
/ch <local|global>
