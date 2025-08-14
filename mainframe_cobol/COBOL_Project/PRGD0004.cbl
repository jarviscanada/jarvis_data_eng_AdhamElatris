       IDENTIFICATION DIVISION.
       PROGRAM-ID. PRGD0004.

       ENVIRONMENT DIVISION.
       INPUT-OUTPUT SECTION.
       FILE-CONTROL.
           SELECT VSAM-FILE ASSIGN TO 'STUDENT.VSAM'
               ORGANIZATION IS INDEXED
               ACCESS MODE IS DYNAMIC
               RECORD KEY IS STUDENT-ID.

       DATA DIVISION.
       FILE SECTION.
       FD VSAM-FILE.
       COPY 'STUDENT.CPY'.

       WORKING-STORAGE SECTION.
       01 CONFIRM                PIC X.

       PROCEDURE DIVISION.
           DISPLAY "ENTER STUDENT ID TO DELETE:"
           ACCEPT STUDENT-ID

           OPEN I-O VSAM-FILE

           READ VSAM-FILE
               INVALID KEY
                   DISPLAY "STUDENT NOT FOUND."
               NOT INVALID KEY
                   DISPLAY "CONFIRM DELETE (Y/N)?"
                   ACCEPT CONFIRM
                   IF CONFIRM = 'Y'
                       DELETE VSAM-FILE
                       DISPLAY "STUDENT DELETED."
                   END-IF
           END-READ

           CLOSE VSAM-FILE
           GOBACK.
