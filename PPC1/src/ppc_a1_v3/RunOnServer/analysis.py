import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns

sns.set_theme(style="whitegrid")


f, ax = plt.subplots(figsize=(6.5, 6.5))
sns.despine(f, left=True, bottom=True)

df = pd.read_csv("records.csv", sep=";")
df.columns = ["TotalCores", "Version", "Time"]
df["Time (s)"] = df['Time'] / 1000000000


#df.plot.scatter(x = 'TotalCores', y = 'Time (s)');
sns.scatterplot(data=df,
                x = "TotalCores",
                y = "Time (s)",
                hue="Version",
                sizes=(1, 8), linewidth=0,
                ax=ax)

plt.savefig('scatter_plot.pdf')