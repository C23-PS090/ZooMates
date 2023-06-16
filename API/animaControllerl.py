from flask import Flask, jsonify
import mysql.connector

def get_hewan():
    try:
        # Establish a connection to the database
        cnx = mysql.connector.connect(
            host='34.101.202.190',
            user='root',
            password='ps090',
            database='hewan'
        )

        # Create a cursor object to execute SQL queries
        cursor = cnx.cursor()

        # Fetch all records from the "hewan" table
        query = "SELECT * FROM hewan"
        cursor.execute(query)
        records = cursor.fetchall()

        # Close the cursor and database connection
        cursor.close()
        cnx.close()

        # Convert records to a list of dictionaries
        hewan_list = []
        for record in records:
            hewan = {
                'id': record[0],
                'nama_hewan': record[1],
                'description': record[2],
                'nama': record[3],
                'jenis': record[4],
                'diet': record[5],
                'makanan': record[6],
                'reproduksi': record[7],
                'tubuh': record[8],
                'kemampuan': record[9],
                'habitat': record[10],
                'pernapasan': record[11],
                'pergerakan': record[12]
            }
            hewan_list.append(hewan)

        # Return the JSON response
        return jsonify(hewan_list)

    except Exception as e:
        return jsonify({'error': str(e)})

