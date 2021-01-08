# Local_Manga_Reader
A tiny little Android app I threw together to read some manga I had saved on my phone. 

Has very strict directory/naming requirements:  
Must have a directory called "Manga" as a top-level directory.  
Inside /Manga are folders, it will use those folders names as the names of the manga  
Inside individual manga folders are chapter folders. These must be numeric  
------Optionally can include a "thumbnail.jpg" (only .jpg cause I'm too lazy to add support for more) alongside chapter folders  
Inside chapter folders are pictures. It will read them lexigraphically, so make sure names are 01, 02, etc unless you want to read 1, 10, 11, 2, 3 order  

If these rules are broken the app will probably just crash cause I threw this together in a few hours and didn't bother with good practices ¯\_(ツ)_/¯
Or if there are unexpected files present it will probably crash too

Also you need to manually give it storage permissions cause I couldn't be bothered to learn how to ask for permissions (this is my first Android app)

What I'm trying to say is this app isn't that hot but hey it does everything I need it to

Maybe I'll revisit this at some point but probably only if I start having problems with it
