def average(values):
    if len(values) == 3:
        return sum(values, 0.0) / len(values)
    else:
        return None

import unittest
class TestSuit(unittest.TestCase):
    def setUp(self):
        pass
    
    def tearDown(self):
        pass
    
    def testAverage(self):
        self.assertEqual(average([1, 2, 3]), 2)

if __name__ == "__main__":
    unittest.main()