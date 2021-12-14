import numpy as np
from fastdtw import fastdtw
from sklearn.base import BaseEstimator, ClassifierMixin

class KnnDtwClassifier(BaseEstimator, ClassifierMixin):
    """pehle DTW laga kr distance nikaal re
    fir dekhre ki konsa sbse paas hai using k nearest neighbour
    mai personally 3 nearest neighbour nikaal kr usme nearest na dekh k majority pick kr raha
    fastdtw use kar re kyuki??? obviously wo fast hai isliye...
    pip install fastdtw
    or if we dont want fast then
    pip install dtw-python
    """
    def __init__(self, n_neighbors=1):
        self.n_neighbors = n_neighbors
        self.features = []
        self.labels = []

    def get_distance(self, x, y):
        return fastdtw(x, y)[0]

    def fit(self, X, y=None):
        for index, l in enumerate(y):
            self.features.append(X[index])
            self.labels.append(l)
        return self

    def predict(self, X):
        dist = np.array([self.get_distance(X, seq) for seq in self.features])
        indices = dist.argsort()[:self.n_neighbors]
        return np.array(self.labels)[indices]

    def predict_ext(self, X):
        dist = np.array([self.get_distance(X, seq) for seq in self.features])
        indices = dist.argsort()[:self.n_neighbors]
        return (dist[indices],
                indices)
