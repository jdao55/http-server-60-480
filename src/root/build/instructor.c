#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <assert.h>

//write grade and name to file
int write_grade();

//get query
int get_query( char * query);

//parse query
void parse_query(char * query);

char *replaceWord(const char *s, const char *oldW, const char *newW);
void  print_dynamic_content();

int grades[14]={-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};

int main()
{
    const char html[] = "<!DOCTYPE html>\n\
<html>\n\
<head>\n\
</head>\n\
<body>\n\
<h1>Instructor Page</h1>\n\
<div>\n\
<form >\n\
<table style=\"width:50%% \" align=\"left\">\n\
<tr>\n\
<th align=\"left\">Student</th>\n\
<th align=\"left\">Binary</th>\n\
<th align=\"left\">Executable</th>\n\
<th align=\"left\">Grade</th>\n\
</tr>\n\
\n";
    const char html_end[] = "</table>\n\
<div style=\"text-align:center\">\n\
<input type=\"submit\" value=\"Submit\">\n\
</div>\n\
</form>\n\
</div>\n\
</body>\n\
</html>";
    char buff[512];
    if (get_query(buff))
    {
        parse_query(buff);
        write_grade();
    }
    printf(html);
    print_dynamic_content();
    printf(html_end);

}

int get_query(char* query)
{
    char buff[512], temp1[49], temp2[45];

    scanf("%s %s %s",temp1, buff, temp2);
    char * temp=strchr(buff, '?');
    if (temp)
    {
        strcpy(query, ++temp);
        return 1;
    }
    return 0;

}
int write_grade()
{
    FILE *fptr1, *fptr2;
    int linectr = 0;
    char str[128];
    char  temp[] = "students/temp.txt";


    fptr1 = fopen("students/grades.txt", "r");
    if (!fptr1)
    {
        return 0;
    }
    fptr2 = fopen(temp, "w");
    if (!fptr2)
    {
        fclose(fptr1);
        return 0;
    }
    // copy all contents to the temporary file other except specific line
    while (!feof(fptr1))
    {
        strcpy(str, "\0");
        fgets(str,128, fptr1);
        if (!feof(fptr1))
        {

            if (grades[linectr]==-1)
            {
                fprintf(fptr2, "%s", str);

            }
            else
            {
                fprintf(fptr2, "%d\n", grades[linectr]);

            }
            linectr++;
        }
    }
    fclose(fptr1);
    fclose(fptr2);
    remove("students/grades.txt");
    rename(temp, "students/grades.txt");

    return 0;
}

void parse_query(char * query)
{
   const char s[2] = "=&";
   char *token;

   /* get the first token */
   token = strtok(query,s);

   /* walk through other tokens */
   while( token != NULL ) {
       int i=atoi(token);
       token = strtok(NULL, s);
       if (token ==NULL)
           break;
       int n=atoi(token);
       grades[i]=n;
   }
}

void print_dynamic_content()
{
    char line1[256], line2[256];
    FILE* grade_file = fopen("students/grades.txt", "r");
    FILE* name_file = fopen("students/classlist.txt", "r");

    for(int i=0;i<14;i++)
    {
        fgets(line1, sizeof(line1), grade_file);
        fgets(line2, sizeof(line2), name_file);
        printf("<tr><td>%s</td>", line2);
        printf("<td><a href=\"/downloads/source%d.c\" download>Source</a></td>", i);
        printf("<td><a href=\"/downloads/binary%d.out\" download>Binary</a></td>", i);
        printf("<td><input type=\"text\" name=\"%d\" value=\"%s\"></td></tr>", i, line1);
    }
}
