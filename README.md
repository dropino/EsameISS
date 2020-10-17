# EsameIss2020

Group project for the course: Software Systems Engineering at the University of Bologna.

Group composed by [Nicholas Carroll](https://github.com/dropino), [Laura Mazzuca](https://github.com/lauramazzuca21) and [Giuseppe Giorgio](https://github.com/gitdevel7).

SPRINT | GOAL
------------ | -------------
10/09/2020 [Sprint1]() | Elaborate Requirement Analysis for the whole Project. Elaborate Problem Analysis and develop Project of a Prototype of the Waiter behavior to control a [Virtual Robot](https://github.com/anatali/iss2020LabBo/tree/master/it.unibo.virtualRobot2020).
17/09/2020 [Sprint2]() | Develop Prototype of a UI to control a Client, implement separation Waiterwalker-Walker, write extended functional tests.
24/09/2020 [Sprint3]() | Complete Prototype of the UI to control a Client, add multi-clients support, write extended functional tests.
--/10/2020 [Sprint4]() | Developed manager interface, fatto refactoring dei controller che ora lavorano con 4 services: *smartbell service*, *waiter service*, *barman service* and *walker service*

### Sprint1 
*To test the Sprint1:*
1. Open Eclipse and import the following file  
  a. **GitHub\EsameIss2020\SPRINTS\tearoom.SPRINT1**  
2. In the workspace SPRINT1/test you can find 3 *junit classes* which test waiter position

### Sprint2  
*To test the Sprint2:*
1. Open Eclipse and import the following file  
  a. **GitHub\EsameIss2020\SPRINTS\tearoom.SPRINT2**  
2. In the workspace SPRINT2/test you can find 3 *junit classes* which test waiter behaviour and waiter position

### Sprint3  
*To test the Sprint3:*
1. Run the *MQTT* broker with **Mosquitto**
2. Run the project *Virtual Robot* (**GitHub\EsameIss2020\SPRINTS\tools\it.unibo.virtualRobot2020\node\WEnv\server\src**) with the command line **node main 8999**  
3. Open your browser and go to **localhost:8090**  
4. Run *.bat* file **"it.unibo.qak20.basicrobot.bat"** in the following folder **GitHub\EsameIss2020\SPRINTS\tools\it.unibo.qak20.basicrobot\build\distributions\it.unibo.qak20.basicrobot-1.0\bin**  
5. Open Eclipse and import the following file  
  a. **GitHub\EsameIss2020\SPRINTS\tearoom.SPRINT3**  
  b. **GitHub\EsameIss2020\SPRINTS\tearoom.SPRINT3.ui**  
6. In the workspace SPRINT3/src run "*ctxtearoom*" as *kotlin application*
7. In the workspace SPRINT3.ui/src run "*Application.java*" as *java application*
8. Open your browser and go to **localhost:8080** and choose *client view* 
8.1 You can also do some tests, without *client view*, to verify waiter behaviour with CoAP message

### Sprint4  
*To test the final system you have to:*
1. Run the *MQTT* broker with **Mosquitto**
2. Run the project *Virtual Robot* (**GitHub\EsameIss2020\SPRINTS\tools\it.unibo.virtualRobot2020\node\WEnv\server\src**) with the command line **node main 8999**  
3. Open your browser and go to **localhost:8090**  
4. Run *.bat* file **"it.unibo.qak20.basicrobot.bat"** in the following folder **GitHub\EsameIss2020\SPRINTS\tools\it.unibo.qak20.basicrobot\build\distributions\it.unibo.qak20.basicrobot-1.0\bin**  
5. Open Eclipse and import the following file  
  a. **GitHub\EsameIss2020\SPRINTS\tearoom.SPRINT4**  
  b. **GitHub\EsameIss2020\SPRINTS\tearoom.SPRINT4.ui**  
6. In the workspace SPRINT4/src run "*ctxtearoom*" as *kotlin application*
7. In the workspace SPRINT4.ui/src run "*Application.java*" as *java application*
8. Open your browser and go to **localhost:8080** and choose *manager view* or *client view* 
9. Enjoy our *Tearoom* 

## Image
