       IDENTIFICATION DIVISION.
       PROGRAM-ID. PRGMENU.

       DATA DIVISION.
       WORKING-STORAGE SECTION.
       01 USER-OPTION         PIC 9.

       PROCEDURE DIVISION.
           PERFORM UNTIL USER-OPTION = 9
               DISPLAY " +-----------------------------------+"
               DISPLAY " |          M A I N   M E N U        |"
               DISPLAY " +-----------------------------------+"
               DISPLAY " |               OPTIONS             |"
               DISPLAY " +-----------------------------------+"
               DISPLAY " |  1 - GENERATE VSAM FILE           |"
               DISPLAY " |  2 - INSERT STUDENT DATA          |"
               DISPLAY " |  3 - UPDATE STUDENT DATA          |"
               DISPLAY " |  4 - DELETE STUDENT DATA          |"
               DISPLAY " |  5 - CLASS QUERY (ALL STUDENTS)   |"
               DISPLAY " |  6 - QUERY STUDENT BY ID          |"
               DISPLAY " |  7 - QUERY BY DATE OF INCLUSION   |"
               DISPLAY " |  8 - REPORT FILE WITH DATE BREAK  |"
               DISPLAY " |  9 - EXIT                         |"
               DISPLAY " +-----------------------------------+"
               DISPLAY "ENTER OPTION: "
               ACCEPT USER-OPTION

               EVALUATE USER-OPTION
                   WHEN 1 CALL 'PRGV0001'
                   WHEN 2 CALL 'PRGI0002'
                   WHEN 3 CALL 'PRGU0003'
                   WHEN 4 CALL 'PRGD0004'
                   WHEN 5 CALL 'PRGQ0005'
                   WHEN 6 CALL 'PRGQ0006'
                   WHEN 7 CALL 'PRGQ0007'
                   WHEN 8 CALL 'PRGR0008'
                   WHEN 9 DISPLAY "GOODBYE."
                   WHEN OTHER DISPLAY "INVALID OPTION"
               END-EVALUATE
           END-PERFORM.

           STOP RUN.
