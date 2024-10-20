The task management system adheres to SOLID and GRASP architecture. Thus, its code is maintainable and easy to change. The task management system has projects, and each project has tasks and team members; the system lets you put both tasks and team members into any project you want. The system has special task types, like high-priority and recurring, that use inheritance and polymorphism. And since each team member plays a role, like team lead, in the system, we also make good use of interfaces to keep the design flexible. 
Here's how each principle is implemented.

Single Responsibility Principle (SRP): 
Each class holds one well-defined responsibility. The Project class, for instance, manages tasks and team members. The Task classes holds all the data associated with tasks. This separation guarantees that changes in any one part of the system do not ripple through other parts, which in turn keeps the system maintainable and testable.

Open/Closed Principle (OCP):
While the system is open to extension, it is closed to modification. The abstract class Task permits the addition of new task types, like HighPriorityTask and RecurringTask, without changing any existing code.  This means that new task types can be introduced easily and almost without risk.

Liskov Substitution Principle (LSP)
Inheritance helps to ensure that subclass components can be used in place of parent components without affecting the system's overall behavior. For example, both the HighPriorityTask and RecurringTask extend the Task class, and both can be used in any context where a Task is expected, yielding consistent behavior throughout the system.

Interface Segregation Principle (ISP)
Small, concentrated interfaces are used throughout the system to guarantee that classes implement only the methods that are relevant to them. The interface ProjectManager is implemented by the class TeamLead, which ensures that only the relevant project management responsibilities are included. This keeps the TeamLead class clean and the codebase free of unnecessary dependencies.

Dependency Inversion Principle (DIP)
The abstract class Task is the parent of several subclasses that define different kinds of tasks. The concrete classes do not change the Project class, but if we had a specific kind of task defined, then our project structure could not be reused. The clearly defined roles and responsibilities of each class in the system provide a nearly ideal way to manage tasks and team members.


Vinay Gupta
Viraj Kulkarni
Dev Patel
Justin Zheng
Anish Cheraku