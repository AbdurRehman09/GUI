import matplotlib.pyplot as plt

# Read data from the "items.txt" file
file_path = r'C:\Users\Abdul Rehman\IdeaProjects\GUI\temp.txt'  # Update the file path as needed
data=[]
try:
    with open(file_path, "r") as file:
        for line in file:
            line = line.strip()  # Remove leading and trailing whitespace
            parts = line.split('-')
            if len(parts) == 7:
                item_type = int(parts[0])
                title = parts[1]
                author = parts[2]
                year = int(parts[3])
                popularity_count = int(parts[4])
                price = int(parts[5])
                text=parts[6]
                data.append((title, popularity_count))
            else:
                print(f"Invalid data in line: {line}")
except FileNotFoundError:
    print("File not found.")

if len(data) < 1:
    print("No valid data found in the file.")
else:
    labels, popularity_count = zip(*data)
    plt.figure(figsize=(6, 6))
    plt.pie(popularity_count, labels=labels, autopct='%1.1f%%', startangle=140)
    plt.title('Popularity Distribution')
    plt.axis('equal')
    plt.show()