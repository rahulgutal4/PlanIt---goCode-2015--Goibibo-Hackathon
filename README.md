# PlanIt---goCode-2015--Goibibo-Hackathon
PlanIt was made for Goibibo hackathon, goCode 2015 held at Bangalore.
PlanIt allows users to do two things:
1. Share their itineraries.
2. Plan their holidays using itineraries shared by others.

Here are two main things:
1. Suggestion of itineraries is made using graph theory algorithms like dfs, bfs for path finding and disjoint sets for determining
   connected components.
2. Ranking of suggested itineraries is made based on 22 parameters. Ranking algorithm also takes care of showing up in the first or
   second page the underrated places or itineraries that are not yet visited much. Algorithm keeps fix slots for this and fills it in
   round robin fashion.
