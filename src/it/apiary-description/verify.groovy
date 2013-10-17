expected = """HOST: http://demoaap.apiary.io

--- Demo Application ---

---
This is a Demo App!

### Request Types
    - GET    : Read a resource or list of resources
    - POST   : Create or Update resource
    - DELETE : Delete a resource
---

"""
apiaryMd = new File(basedir, "Apiary.md")

assert apiaryMd.exists()
assert apiaryMd.getText().equals(expected)