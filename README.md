# Slotter

Interview waiting rooms are broken.
You're told your slot is 11am. It's 1pm. You're still waiting.
Nobody knows when the person before you will finish.

Slotter fixes this by learning from how long interviews
actually take — and telling the next person when to show up.

## What it does
Organisers create an event with time slots.
Applicants register and pick a slot.

As interviews happen, the app tracks elapsed time for each one.
It uses that pattern to predict when the next slot will actually
start — not when it was scheduled.

Candidates get notified when their real estimated time approaches.
No more sitting in a hallway for 2 hours.

## Why this is different from a basic scheduler
It adapts in real time.
If the first 3 interviews each ran 20 minutes over,
it adjusts everyone else's estimate accordingly.

## Tech
Android app
Real-time elapsed time tracking
Push notifications
