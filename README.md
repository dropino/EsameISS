# EsameIss2020

Group project for the course: Software Systems Engineering at the University of Bologna.

Group composed by [Nicholas Carroll](https://github.com/dropino), [Laura Mazzuca](https://github.com/lauramazzuca21) and [Giuseppe Giorgio](https://github.com/gitdevel7).

SPRINT | GOAL
------------ | -------------
10/09/2020 | Elaborate Requirement Analysis for the whole Project. Elaborate Problem Analysis and develop Project of a Prototype of the Waiter behavior to control a [Virtual Robot](https://github.com/anatali/iss2020LabBo/tree/master/it.unibo.virtualRobot2020).
17/09/2020 | Develop Prototype of a UI to control a Client, implement separation Waiterwalker-Walker, write extended functional tests.
24/09/2020 | Complete Prototype of the UI to control a Client, add multi-clients support.

We decided to develop 4 sprint. In each sprint we upgrade our Project  

Sprint | Comment
------------ | -------------
[Sprint1]() | In this sprint we abbiamo studiato e analizzato la traccia definendo un primo prototipo del nostro progetto. Questo studio ci ha permesso di effettuare un'analisi dei requisiti e del problema che ci hanno portato a sviluppare gli attori principali e i loro stati.
[Sprint2]() | Nel secondo sprint abbiamo trovato opportuno modificare alcuni attori, come ad esempio *Waiterwalker -> Waiterwalker + Walker* che ci ha permesso di analizzare al meglio alcune problematiche. Inoltre abbiamo iniziato a pensare all'interfaccia grafica oltre a sviluppare i test.
[Sprint3]() | Sprint decisivo per il nostro progetto. Abbiamo implementato l'interfaccia e perfezionato l'interazione tra cliente e tearoom, in particolar modo è sttao implementata la funzione di gestire l'arrivo di più clienti.
[Sprint4]() | Sprint per definire al meglio il comportamento di tutti gli attori e dei loro stati. L'interazione con i clienti è stata migliorata e sono state definiti gli ultimi dettagli per rendere il nostro prototipo quanto più affidabile possibile. 

 
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
