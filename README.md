# Hashtaag

Library used :

1 Navigation component
2 Hilt Dependency injection
3 View binding
4 LiveData
5 ViewBinding
6 Glide
7 Room Database
8 Retrofit
9 Coroutine

Used MVVM architecture.

There are three modules in this project , "App" , "MyApiServicesModule"  and "localDatabase" . "App" is the default module and a separate
module is being created for storing the class related to Api Services and a seprate module i.e "localDatabase"
for storing the data in roomdatabase . Used navigation component to achieve a single activity app eventhough here only one screen is there
but still i have used navigation component just to demonstrate how would i have done if there is more than one screen .

Initially when you launch the app then firstime api is called and then image is saved into room database and then from second time
api won't get called , image will be fetched from room database .