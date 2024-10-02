package com.example.team.helpers

import com.example.team.helpers.model.LinkedTeam
import com.example.team.helpers.model.TestTeam
import com.example.user.helpers.TestUser

val listOfGetMembers = listOf(
    TestUser(1, "Henk", "van", "Jongen"),
    TestUser(2, "Pieter", "", "Brood"),
    TestUser(3, "Jannick", "van der", "Naaien"),
    TestUser(4, "Grietje", "den", "Bos"),
    TestUser(5, "Zelma", "", "Voorten"),
)

val listOfPostMembers = listOf(
    TestUser(1, "Henk", "van", "Jongen", "henkiscool@gmail.com", "password123!"),
    TestUser(2, "Kaas", "van", "Kees", "kaaskees@gmail.com", "mijnwachtwoord!"),
    TestUser(3, "Joep", "van", "Loep", "joeploep@gmail.com", "joeploep123!")
)

val linkedGETTeams = listOf(
    LinkedTeam(TestTeam("Bug Bytes"), listOf(1)),
    LinkedTeam(TestTeam("AppelHuis"), listOf(2)),
    LinkedTeam(TestTeam("Fire Mites"), listOf()),
    LinkedTeam(TestTeam("EasyCoding"), listOf(2, 3, 4)),
    LinkedTeam(TestTeam("FireBeats"), listOf(4))
)

val linkedPOSTTeams = listOf(
    LinkedTeam(TestTeam("FireBeats"), listOf(1))
)