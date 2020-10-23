# EsameIss2020

Group project for the course: **Software Systems Engineering** at the University of Bologna.

SPRINT | GOAL
------------ | -------------
10/09/2020 [Sprint1]() | Elaborate Requirement Analysis for the whole Project. Elaborate Problem Analysis and develop Project of a Prototype of the Waiter behavior to control a [Virtual Robot](https://github.com/anatali/iss2020LabBo/tree/master/it.unibo.virtualRobot2020).
17/09/2020 [Sprint2]() | Develop Prototype of a UI to control a Client, implement separation Waiterwalker-Walker, write extended functional tests.
28/09/2020 [Sprint3]() | Complete Prototype of the UI to control a Client, add multi-clients support, write extended functional tests.
22/10/2020 [Sprint4]() | Developed manager interface, refactoring of Server Controllers 4 services: *smartbell service*, *waiter service*, *barman service* and *walker service*, Basicrobot propagates collision cause to be handled by Walker to reposition on map based on obstacle found.


## Required libraries and tools version
**Kotlin** *version 1.3.61*  
**Gradle** *version 6.2.2*  
**JVM** *version 1.8.0_212 (Oracle Corporation 25.212-b10)*  
**Eclipse IDE**  *version with DSL tools* 
### Eclipse plugin 
To use Qak we move in Eclipse *"dropins"* folder those plugin:
 1. 	it.unibo.Qactork	(v. 1.2.4)  
 2. 	it.unibo.Qactork.ide	(v. 1.2.4)  
 3. 	it.unibo.Qactork.ui	(v. 1.2.4)  
 
 
## Sprint1 
*To test the Sprint1:*
1. Open Eclipse and import the following project  
  a. [**tearoom.SPRINT1**](https://github.com/lauramazzuca21/EsameIss2020/tree/sprint4/SPRINTS/tearoom.SPRINT1) 
2. In the workspace [test](https://github.com/lauramazzuca21/EsameIss2020/tree/sprint4/SPRINTS/tearoom.SPRINT1/test) you can find 3 *junit classes* which test waiter position

## Sprint2  
*To test the Sprint2:*
1. Open Eclipse and import the following project  
  a. [**tearoom.SPRINT2**](https://github.com/lauramazzuca21/EsameIss2020/tree/sprint4/SPRINTS/tearoom.SPRINT2)  
2. In the workspace [test](https://github.com/lauramazzuca21/EsameIss2020/tree/sprint4/SPRINTS/tearoom.SPRINT2/test) you can find 3 *junit classes* which test waiter behaviour and waiter position

## Sprint3  
*To test the Sprint3:*
1. Run the *MQTT* broker with **Mosquitto**
2. Run the project [*Virtual Robot*](https://github.com/lauramazzuca21/EsameIss2020/tree/it.unibo.virtualRobot2020/node/WEnv/server/src) with the command line **node main 8999**  
3. Open your browser and go to **localhost:8090**  
4. Run *.bat* file **"it.unibo.qak20.basicrobot.bat"** in the following folder [tools](SPRINTS\tools\it.unibo.qak20.basicrobot\build\distributions\it.unibo.qak20.basicrobot-1.0\bin) 
5. Open Eclipse and import the following projects  
  a. [**tearoom.SPRINT3**](https://github.com/lauramazzuca21/EsameIss2020/tree/sprint4/SPRINTS/tearoom.SPRINT3)  
  b. [**tearoom.SPRINT3.ui**](https://github.com/lauramazzuca21/EsameIss2020/tree/sprint4/SPRINTS/tearoom.SPRINT3.ui)  
6. In the workspace [SPRINT3/src](https://github.com/lauramazzuca21/EsameIss2020/tree/sprint4/SPRINTS/tearoom.SPRINT3/src) run "*MainCtxtearoom.kt*" as *kotlin application*
7. In the workspace [SPRINT3.ui/src](https://github.com/lauramazzuca21/EsameIss2020/tree/sprint4/SPRINTS/tearoom.SPRINT3.ui/src) run "*Application.java*" as *java application*
8. Open your browser and go to **localhost:8080** and choose *client view*  
  8.1 You can also run some [tests](https://github.com/lauramazzuca21/EsameIss2020/tree/sprint4/SPRINTS/tearoom.SPRINT3/test), without *client view*, to verify waiter behaviour with CoAP message

## Sprint4  
*To test the final system you have to:*
1. Run the *MQTT* broker with **Mosquitto**
2. Run the project Virtual Robot in EsameIss2020\SPRINTS\tools\it.unibo.virtualRobot2020\node\WEnv\server\src with the command line **node main 8999**  
3. Open your browser and go to **localhost:8090**  
4. Run *.bat* file **"it.unibo.qak20.basicrobot.bat"** in the following folder EsameIss2020\SPRINTS\tools\it.unibo.qak20.basicrobot\build\distributions\it.unibo.qak20.basicrobot-1.0\bin  
5. Open Eclipse and import the following projects  
  a. [**tearoom.SPRINT4**](https://github.com/lauramazzuca21/EsameIss2020/tree/sprint4/SPRINTS/tearoom.SPRINT4)  
  b. [**tearoom.SPRINT4.ui**](https://github.com/lauramazzuca21/EsameIss2020/tree/sprint4/SPRINTS/tearoom.SPRINT3.ui)   
6. Import *java.util* libraries in **SPRINT4\it.unibo.waiter**    
7. In the workspace [SPRINT4/src](https://github.com/lauramazzuca21/EsameIss2020/tree/sprint4/SPRINTS/tearoom.SPRINT4) run "*MainCtxtearoom.kt*" as *kotlin application*
8. In the workspace [SPRINT4.ui/src](https://github.com/lauramazzuca21/EsameIss2020/tree/sprint4/SPRINTS/tearoom.SPRINT4.ui) run "*Application.java*" as *java application*
9. Open your browser and go to **localhost:8080** and choose *manager view* or *client view* 
10. Enjoy our *Tearoom* 

![MainMenuPage](https://github.com/lauramazzuca21/EsameIss2020/blob/sprint4/UserDocs/interfaccia.png)


### Group composed by:   

[Nicholas Carroll](https://github.com/dropino) | [Laura Mazzuca](https://github.com/lauramazzuca21) | [Giuseppe Giorgio](https://github.com/gitdevel7)
------------ | ------------ | -------------
![Nicholas](https://github.com/lauramazzuca21/EsameIss2020/blob/sprint4/UserDocs/OurPics/nicholas.png) | ![Laura](https://github.com/lauramazzuca21/EsameIss2020/blob/sprint4/UserDocs/OurPics/laura.png) | ![Giuseppe](https://github.com/lauramazzuca21/EsameIss2020/blob/sprint4/UserDocs/OurPics/giuseppe.png)   
nicholas.carroll@studio.unibo.it | laura.mazzuca@studio.unibo.it | giuseppe.giorgio3@studio.unibo.it
